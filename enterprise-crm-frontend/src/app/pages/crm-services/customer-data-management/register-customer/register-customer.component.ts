import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerProfile } from '../../../../models/CustomerProfile';
import { CustomersService } from '../service/customers.service';
import { MessageService } from 'primeng/api';
import { Store } from '@ngrx/store';
import { Notification } from '../../../../models/Notification';
import { addNotification } from '../../../../store/notifications/notiffications.actions';

@Component({
    selector: 'app-register-customer',
    standalone: false,
    templateUrl: './register-customer.component.html',
    styleUrl: './register-customer.component.scss'
})
export class RegisterCustomerComponent implements OnInit {
    registerForm!: FormGroup;
    loading = false;
    error: string | null = null;
    success = false;
    selected!: number;

    // Define the options for the dropdowns
    interestOptions: string[] = [
        'MUSIC',
        'TECH',
        'FASHION',
        'SPORTS',
        'ACADEMICS',
        'HOME_APPLIANCES',
        'GIFT',
        'ESSENTIALS',
        'TRAVEL',
        'FOOD_AND_DRINK',
        'HEALTH_AND_WELLNESS',
        'ART_AND_CRAFTS',
        'GAMING',
        'AUTOMOTIVE',
        'PETS',
        'GARDENING',
        'FINANCE',
        'BEAUTY_AND_COSMETICS',
        'BOOKS_AND_LITERATURE',
        'MOVIES_AND_TV',
        'PHOTOGRAPHY',
        'DIY_AND_HOME_IMPROVEMENT',
        'OUTDOOR_ACTIVITIES',
        'SOCIAL_MEDIA',
        'FITNESS',
        'ENVIRONMENT',
        'SCIENCE_AND_TECHNOLOGY'
    ];

    regionOptions: string[] = [
        'NORTH_AMERICA',
        'LATIN_AMERICA',
        'ASIAN_PACIFIC',
        'MIDDLE_EAST_AND_AFRICA',
        'CENTRAL_AMERICA',
        'EUROPE',
        'NORTH',
        'EASTERN_EUROPE',
        'WESTERN_EUROPE',
        'NORTHERN_EUROPE',
        'SOUTHERN_EUROPE',
        'SOUTHEAST_ASIA',
        'SOUTH_ASIA',
        'EAST_ASIA',
        'OCEANIA',
        'SUB_SAHARAN_AFRICA'
    ];

    constructor(
        private fb: FormBuilder,
        private customersService: CustomersService,
        private router: Router,
        private messageService: MessageService,
        private store: Store
    ) {}

    ngOnInit(): void {
        this.registerForm = this.fb.group({
            name: ['', Validators.required],
            emailId: ['', [Validators.required, Validators.email]],
            phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
            purchaseHistory: [[]],
            segmentationData: this.fb.group({
                Interest: ['', Validators.required],
                Region: ['', Validators.required],
                'Purchasing Habits': ['NEW']
            })
        });
    }

    onSubmit(): void {
        if (this.registerForm.valid) {
            this.loading = true;
            this.error = null;
            this.success = false;

            const customerProfile: CustomerProfile = this.registerForm.value;

            this.customersService.registerCustomer(customerProfile).subscribe({
                next: (response) => {
                    console.log('Customer registered successfully:', response);
                    this.loading = false;
                    this.messageService.add({ severity: 'info', summary: 'Success.', detail: 'Customer Registered Successfully.' });
                    this.success = true;
                    const newNotification: Notification = {
                        heading: 'Customer Data Management',
                        description: `Registered a new customer with ID: ${response.customerID}.`,
                        time: new Date().toLocaleTimeString()
                    };
                    this.store.dispatch(addNotification({ notification: newNotification }));
                    this.router.navigate(['pages/services/customer-data-management']);
                },
                error: (error) => {
                    this.error = error.message || 'Failed to register customer.';
                    this.loading = false;
                    this.messageService.add({ severity: 'info', summary: 'Error.', detail: 'Some Error Occurred.' });
                    this.success = false;
                }
            });
        } else {
            this.markFormGroupTouched(this.registerForm);
        }
    }

    markFormGroupTouched(formGroup: FormGroup) {
        (Object as any).values(formGroup.controls).forEach((control: any) => {
            control.markAsTouched();
            if (control.controls) {
                this.markFormGroupTouched(control);
            }
        });
    }
}
