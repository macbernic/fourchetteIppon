import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { RestaurantService } from '../entities/restaurant/restaurant.service';
import { Restaurant } from '../entities/restaurant/restaurant.model';

import { Account, LoginModalService, Principal, ITEMS_PER_PAGE } from '../shared';
import {ResponseWrapper} from "../shared/model/response-wrapper.model";

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    restaurants: Restaurant[];
    itemsPerPage: number;
    links: any;
    page: any;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private restaurantService: RestaurantService,
        private jhiAlertService: JhiAlertService
    ) {
        this.restaurants = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };

    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.loadRestaurant();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }


    reset() {
        this.page = 0;
        this.restaurants = [];
        this.loadRestaurant();
    }

    loadRestaurant() {
        this.restaurantService.query().subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    private onSuccess(data, headers) {
        for (let i = 0; i < data.length; i++) {
            this.restaurants.push(data[i]);
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    ngOnDestroy() {
        //this.eventManager.destroy(this.eventSubscriber);
    }

}
