import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RestaurantComponent } from './restaurant.component';
import { RestaurantDetailComponent } from './restaurant-detail.component';
import { RestaurantPopupComponent } from './restaurant-dialog.component';
import { RestaurantDeletePopupComponent } from './restaurant-delete-dialog.component';

export const restaurantRoute: Routes = [
    {
        path: 'restaurant',
        component: RestaurantComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Restaurants'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'restaurant/:id',
        component: RestaurantDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Restaurants'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const restaurantPopupRoute: Routes = [
    {
        path: 'restaurant-new',
        component: RestaurantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Restaurants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'restaurant/:id/edit',
        component: RestaurantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Restaurants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'restaurant/:id/delete',
        component: RestaurantDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Restaurants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
