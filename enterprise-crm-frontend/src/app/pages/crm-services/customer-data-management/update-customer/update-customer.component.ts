import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerProfile } from '../../../../models/CustomerProfile';
import { CustomersService } from '../service/customers.service';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-update-customer',
    standalone: false,
    templateUrl: './update-customer.component.html',
    styleUrl: './update-customer.component.scss'
})
export class UpdateCustomerComponent {
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

    customerId!: number | null;
    customerData!: CustomerProfile;
    constructor(
        private fb: FormBuilder,
        private customersService: CustomersService,
        private router: Router,
        private route: ActivatedRoute,
        private readonly messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.customerId = Number(this.route.snapshot.paramMap.get('id'));

        console.log(this.customerId);
        this.getCustomerData();
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

    getCustomerData() {
        this.customersService.getCustomerById(this.customerId).subscribe((data) => {
            this.customerData = data;
            console.log(this.customerData);
            this.registerForm.patchValue(this.customerData);
        });
    }

    onSubmit(): void {
        if (this.registerForm.valid) {
            this.loading = true;
            this.error = null;
            this.success = false;

            let customerProfile: CustomerProfile = this.registerForm.value;
            customerProfile.customerID = this.customerId;

            this.customersService.updateCustomer(customerProfile).subscribe({
                next: (response) => {
                    console.log('Customer registered successfully:', response);
                    this.loading = false;
                    this.messageService.add({ severity: 'info', summary: 'Success.', detail: 'Customer Updated Successfully.' });
                    this.success = true;
                    this.router.navigate(['pages/services/customer-data-management']);
                },
                error: (error) => {
                    this.error = error.message || 'Failed to register customer.';
                    this.loading = false;
                    this.messageService.add({ severity: 'error', summary: 'Success.', detail: 'Some error occurred.' });
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
