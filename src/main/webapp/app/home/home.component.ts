import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Restaurant } from '../entities/restaurant/restaurant.model';
import { RestaurantService } from '../entities/restaurant/restaurant.service';
import { Account, LoginModalService, Principal, ITEMS_PER_PAGE, ResponseWrapper } from '../shared';

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
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private restaurantService: RestaurantService,
        private jhiAlertService: JhiAlertService,
        private parseLinks: JhiParseLinks
    ) {
        this.restaurants = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        this.restaurantService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    reset() {
        this.page = 0;
        this.restaurants = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.account = account;
            this.currentAccount = account;
        });
        this.registerAuthenticationSuccess();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Restaurant) {
        return item.id;
    }
    registerChangeInRestaurants() {
        this.eventSubscriber = this.eventManager.subscribe('restaurantListModification', (response) => this.reset());
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

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.restaurants.push(data[i]);
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
