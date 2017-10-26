import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Restaurant } from './restaurant.model';
import { RestaurantPopupService } from './restaurant-popup.service';
import { RestaurantService } from './restaurant.service';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-restaurant-dialog',
    templateUrl: './restaurant-dialog.component.html'
})
export class RestaurantDialogComponent implements OnInit {

    restaurant: Restaurant;
    isSaving: boolean;

    locations: Location[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private restaurantService: RestaurantService,
        private locationService: LocationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.locationService
            .query({filter: 'restaurant-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.restaurant.locationId) {
                    this.locations = res.json;
                } else {
                    this.locationService
                        .find(this.restaurant.locationId)
                        .subscribe((subRes: Location) => {
                            this.locations = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.restaurant.id !== undefined) {
            this.subscribeToSaveResponse(
                this.restaurantService.update(this.restaurant));
        } else {
            this.subscribeToSaveResponse(
                this.restaurantService.create(this.restaurant));
        }
    }

    private subscribeToSaveResponse(result: Observable<Restaurant>) {
        result.subscribe((res: Restaurant) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Restaurant) {
        this.eventManager.broadcast({ name: 'restaurantListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-restaurant-popup',
    template: ''
})
export class RestaurantPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private restaurantPopupService: RestaurantPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.restaurantPopupService
                    .open(RestaurantDialogComponent as Component, params['id']);
            } else {
                this.restaurantPopupService
                    .open(RestaurantDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
