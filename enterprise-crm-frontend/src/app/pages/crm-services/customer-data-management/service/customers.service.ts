import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { CustomerProfile } from '../../../../models/CustomerProfile';

@Injectable({
    providedIn: 'root'
})
export class CustomersService {
    apiUrl = environment.apiUrl;
    updateCustomerID!: number | null;

    constructor(private http: HttpClient) {}

    private retriveCustomerProfileURL = this.apiUrl + '/customers';
    private registerCustomerURL = this.apiUrl + '/customers';
    private updateCustomerURL = this.apiUrl + '/customers';
    private deleteCustomerURL = this.apiUrl + '/customers';
    private searchCustomerURL = this.apiUrl + '/customers';

    public getCustomers() {
        return this.http.get<CustomerProfile[]>(this.retriveCustomerProfileURL);
    }

    public getCustomerById(customerId: number | null) {
        return this.http.get<CustomerProfile>(this.retriveCustomerProfileURL + '/' + customerId);
    }

    public registerCustomer(customerProfile: any) {
        return this.http.post<CustomerProfile>(this.registerCustomerURL, customerProfile);
    }

    public updateCustomer(CustomerProfile: any) {
        return this.http.put<CustomerProfile>(this.updateCustomerURL + '/' + CustomerProfile.customerID, CustomerProfile);
    }

    public deleteCustomer(customerID: number) {
        return this.http.delete(this.deleteCustomerURL + '/' + customerID, { responseType: 'text' });
    }

    public setCustomerId(customerID: number | null) {
        this.updateCustomerID = customerID;
    }

    public getCustomerId() {
        return this.updateCustomerID;
    }
}
