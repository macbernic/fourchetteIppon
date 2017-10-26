import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FourchetteIpponSharedModule } from '../shared';
import {
    RestaurantService,
    RestaurantPopupService,
    RestaurantComponent,
    RestaurantDetailComponent,
    RestaurantDialogComponent,
    RestaurantPopupComponent,
    RestaurantDeletePopupComponent,
    RestaurantDeleteDialogComponent,
    restaurantRoute,
    restaurantPopupRoute,
} from './../entities/restaurant/';

const ENTITY_STATES = [
    ...restaurantRoute,
    ...restaurantPopupRoute,
];

import { HOME_ROUTE, HomeComponent } from './';

@NgModule({
    imports: [
        FourchetteIpponSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HomeComponent,
        RestaurantComponent,
        RestaurantDetailComponent,
        RestaurantDialogComponent,
        RestaurantDeleteDialogComponent,
        RestaurantPopupComponent,
        RestaurantDeletePopupComponent,
    ],
    entryComponents: [
        RestaurantComponent,
        RestaurantDialogComponent,
        RestaurantPopupComponent,
        RestaurantDeleteDialogComponent,
        RestaurantDeletePopupComponent,
    ],
    providers: [
        RestaurantService,
        RestaurantPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FourchetteIpponHomeModule {}
