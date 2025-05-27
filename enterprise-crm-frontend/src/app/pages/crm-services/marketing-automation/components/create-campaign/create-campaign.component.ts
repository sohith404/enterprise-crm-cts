import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { MarketingAutomationService } from '../../service/marketing-automation.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Store } from '@ngrx/store';
import { Notification } from '../../../../../models/Notification';
import { addNotification } from '../../../../../store/notifications/notiffications.actions';

// Custom validator for future dates
function futureDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
        return null; // Don't validate if no value is provided (required handles this)
    }
    const selectedDate = new Date(control.value);
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0); // Compare only dates

    if (selectedDate < currentDate) {
        return { futureDateValidator: true };
    }
    return null;
}

// Custom validator to prevent numerical names
function nonNumericalNameValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
        return null;
    }
    if (!isNaN(parseFloat(control.value)) && isFinite(control.value)) {
        return { pattern: true };
    }
    return null;
}

@Component({
    selector: 'app-create-campaign',
    standalone: false,
    templateUrl: './create-campaign.component.html',
    styleUrl: './create-campaign.component.scss'
})
export class CreateCampaignComponent implements OnInit {
    loading = false;
    formData: FormGroup;
    successMessageVisible: boolean = false;
    showForm: boolean = true; // Controls form visibility (initially visible)

    constructor(
        private fb: FormBuilder,
        private marketingService: MarketingAutomationService,
        private router: Router,
        private readonly messageService: MessageService,
        private readonly store: Store
    ) {
        this.formData = this.fb.group({
            name: ['', [Validators.required, Validators.minLength(5), nonNumericalNameValidator]],
            startDate: ['', [Validators.required, futureDateValidator]],
            endDate: ['', [Validators.required, futureDateValidator]],
            type: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        // You can add any initialization logic here
    }

    get name() {
        return this.formData.get('name');
    }
    get startDate() {
        return this.formData.get('startDate');
    }
    get endDate() {
        return this.formData.get('endDate');
    }
    get type() {
        return this.formData.get('type');
    }

    onSubmit(): void {
        if (this.formData.valid) {
            this.loading = true;
            this.marketingService
                .registerCampaign({
                    name: this.formData.get('name')?.value,
                    startDate: this.formData.get('startDate')?.value,
                    endDate: this.formData.get('endDate')?.value,
                    type: this.formData.get('type')?.value
                })
                .subscribe({
                    next: (response) => {
                        console.log('Campaign created successfully:', response);
                        this.successMessageVisible = true;
                        // Optionally reset the form after successful submission
                        this.formData.reset();
                        this.messageService.add({ severity: 'info', summary: 'Success.', detail: 'Campaign Created Successfully.' });
                        // Optionally navigate to the campaign list or another page
                        // this.router.navigate(['/campaigns']);
                        const newNotification: Notification = {
                            heading: 'Marketing Automation',
                            description: `Created a new campaign with ID: ${response.campaignID}.`,
                            time: new Date().toLocaleTimeString()
                        };
                        this.store.dispatch(addNotification({ notification: newNotification }));
                        this.loading = false;
                    },
                    error: (error) => {
                        console.error('Error creating campaign:', error);
                        this.messageService.add({ severity: 'error', summary: 'Error.', detail: 'Some error occurred.' });
                        // Handle error display to the user
                        this.loading = false;
                    }
                });
        } else {
            // Trigger validation to show error messages
            Object.values(this.formData.controls).forEach((control) => {
                if (control.invalid) {
                    control.markAsDirty();
                    control.updateValueAndValidity({ onlySelf: true });
                }
            });
        }
    }

    successMessage(): void {
        this.successMessageVisible = true;
        setTimeout(() => {
            this.successMessageVisible = false;
        }, 3000); // Hide after 3 seconds
    }

    cancelForm(): void {
        this.showForm = false;
        // Optionally navigate back to the campaign list or another page
        this.router.navigate(['pages/services/marketing-automation']);
    }
}
