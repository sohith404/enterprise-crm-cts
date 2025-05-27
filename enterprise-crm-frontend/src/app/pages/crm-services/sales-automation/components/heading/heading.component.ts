import { Component, Input, OnInit } from '@angular/core';
import { SalesOpportunity } from '../../../../../models/SalesOpportunity';
import { Observable } from 'rxjs';
import { MenuItem, MessageService } from 'primeng/api';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SalesService } from '../../service/sales.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Notification } from '../../../../../models/Notification';
import { addNotification } from '../../../../../store/notifications/notiffications.actions';

@Component({
    selector: 'app-heading',
    standalone: false,
    templateUrl: './heading.component.html',
    styleUrl: './heading.component.scss'
})
export class HeadingComponent implements OnInit {
    @Input({ required: true }) title!: string;
    @Input({ required: true }) allSales$!: Observable<SalesOpportunity[]>;

    constructor(
        private readonly fb: FormBuilder,
        private readonly salesService: SalesService,
        private readonly messageService: MessageService,
        private readonly store: Store
    ) {}

    ngOnInit(): void {
        this.newLeadForm = this.fb.group({
            customerId: ['', [Validators.required, Validators.min(1)]],
            estimatedValue: [10000, [Validators.required, Validators.min(10000)]],
            closingDate: [
                null,
                [
                    Validators.required,
                    (control: FormControl) => {
                        const selectedDate = new Date(control.value);
                        const today = new Date();
                        const oneWeekFromToday = new Date(today.setDate(today.getDate() + 7));
                        return selectedDate >= oneWeekFromToday
                            ? null
                            : {
                                  invalidDate: 'Closing date must be at least 1 week from today'
                              };
                    }
                ]
            ],
            salesStage: [{ name: 'PROSPECTING' }]
        });

        this.notificationCronForm = this.fb.group({
            time: [null, [Validators.required]]
        });

        this.closingCronForm = this.fb.group({
            time: [null, [Validators.required]]
        });

        this.items = [
            {
                icon: 'pi pi-calendar',
                label: 'Schedule Lead Closing Time',
                command: () => {
                    this.showDialog1();
                }
            },
            {
                icon: 'pi pi-clock',
                label: 'Schedule Notification Time',
                command: () => {
                    this.showDialog2();
                }
            },
            {
                icon: 'pi pi-plus',
                label: 'Create New Lead',
                command: () => {
                    this.showDialog3();
                }
            }
        ];
        this.allSales$.subscribe((sales) => {
            this.activeLeads = sales.filter((sale) => (sale.salesStage !== 'CLOSED_LOST' || 'CLOSED_WON') && Date.parse(sale.closingDate) > Date.now()).length;
            this.estimatedRevenue = sales.reduce((total, sale) => total + (sale.estimatedValue || 0), 0);
            this.leadsWon = sales.filter((sale) => sale.salesStage === 'CLOSED_WON').length;
        });
    }

    activeLeads!: number;
    estimatedRevenue!: number;
    leadsWon!: number;
    items!: MenuItem[];
    newLeadForm!: FormGroup;
    notificationCronForm!: FormGroup;
    closingCronForm!: FormGroup;

    visible1: boolean = false;
    visible2: boolean = false;
    visible3: boolean = false;

    isLoading = false;

    timeToDailyCron(timeString: string) {
        const date = new Date(timeString);

        const minutes = date.getMinutes();
        const hours = date.getHours();

        return `* ${minutes} ${hours} * * * `;
    }

    showDialog1() {
        this.visible1 = true;
    }

    showDialog2() {
        this.visible2 = true;
    }

    showDialog3() {
        this.visible3 = true;
    }

    showToast(toast: { severity: string; summary: string; message: string }) {
        this.messageService.add({ severity: toast.severity, summary: toast.summary, detail: toast.message });
    }

    salesStage = [{ name: 'PROSPECTING' }];

    handleLeadSubmit() {
        this.isLoading = true;
        const date = this.newLeadForm.get('closingDate')?.value;
        const formattedDate = date.toISOString().split('T')[0];

        this.salesService
            .create({
                customerID: this.newLeadForm.get('customerId')?.value,
                estimatedValue: this.newLeadForm.get('estimatedValue')?.value,
                closingDate: formattedDate,
                salesStage: 'PROSPECTING'
            })
            .subscribe({
                next: (sale: SalesOpportunity) => {
                    this.showToast({
                        severity: 'success',
                        summary: 'Saved',
                        message: `Lead created successfully with id: ${sale.opportunityID}`
                    });
                    const newNotification: Notification = {
                        heading: 'Sales Automation',
                        description: `New lead created with ID: ${sale.opportunityID}.`,
                        time: new Date().toLocaleTimeString()
                    };
                    this.store.dispatch(addNotification({ notification: newNotification }));

                    this.salesService.refreshSalesList();
                },
                error: (error: HttpErrorResponse) => {
                    this.showToast({
                        severity: 'error',
                        summary: 'Error',
                        message: error.error.message
                    });
                    this.isLoading = false;
                },
                complete: () => {
                    this.isLoading = false;
                    this.visible3 = false;
                    this.newLeadForm.reset({
                        salesStage: [{ name: 'PROSPECTING' }],
                        estimatedValue: 10000
                    });
                }
            });
    }

    handleNotificationSumbit() {
        this.isLoading = true;
        const timeString = this.notificationCronForm.get('time')?.value;
        const cron = this.timeToDailyCron(timeString);
        this.salesService.updateNotificationSchedule(cron).subscribe({
            next: (res: { id: string; taskName: string; cronExpression: string }) => this.showToast({ severity: 'success', summary: 'Success', message: `Updated schedule for ${res.taskName}` }),
            error: (error: HttpErrorResponse) => {
                this.showToast({ severity: 'error', summary: 'Error', message: error.error.message });
                this.notificationCronForm.reset();
            },
            complete: () => {
                this.isLoading = false;
                this.visible2 = false;
                this.notificationCronForm.reset();
            }
        });
    }
    handleClosingSumbit() {
        this.isLoading = true;
        const timeString = this.closingCronForm.get('time')?.value;
        const cron = this.timeToDailyCron(timeString);
        this.salesService.updateClosingSchedule(cron).subscribe({
            next: (res: { id: string; taskName: string; cronExpression: string }) => this.showToast({ severity: 'success', summary: 'Success', message: `Updated schedule for ${res.taskName}` }),
            error: (error: HttpErrorResponse) => {
                this.showToast({ severity: 'error', summary: 'Error', message: error.error.message });
                this.closingCronForm.reset();
            },
            complete: () => {
                this.isLoading = false;
                this.visible1 = false;
                this.notificationCronForm.reset();
            }
        });
    }

    get customerIdErrorMessage(): string {
        const control = this.newLeadForm.get('customerId');
        if (control?.hasError('required')) {
            return 'Customer ID is required.';
        }
        if (control?.hasError('min')) {
            return 'Customer ID must be valid';
        }
        return '';
    }

    get estimatedValueErrorMessage(): string {
        const control = this.newLeadForm.get('estimatedValue');
        if (control?.hasError('required')) {
            return 'Estimated value is required.';
        }
        if (control?.hasError('min')) {
            return 'Estimated value must be at least 10,000.';
        }
        return '';
    }

    get closingDateErrorMessage(): string {
        const control = this.newLeadForm.get('closingDate');
        if (control?.hasError('required')) {
            return 'Closing date is required.';
        }
        if (control?.hasError('invalidDate')) {
            return control.getError('invalidDate');
        }
        return '';
    }

    get notificationTimeErrorMessage(): string {
        const control = this.notificationCronForm.get('time');
        if (control?.hasError('required')) {
            return 'Notification time is required.';
        }
        return '';
    }

    get closingTimeErrorMessage(): string {
        const control = this.closingCronForm.get('time');
        if (control?.hasError('required')) {
            return 'Closing time is required.';
        }
        return '';
    }
}
