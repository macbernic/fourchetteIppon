import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ContributeurComponent } from './contributeur.component';
import { ContributeurDetailComponent } from './contributeur-detail.component';
import { ContributeurPopupComponent } from './contributeur-dialog.component';
import { ContributeurDeletePopupComponent } from './contributeur-delete-dialog.component';

export const contributeurRoute: Routes = [
    {
        path: 'contributeur',
        component: ContributeurComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Contributeurs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contributeur/:id',
        component: ContributeurDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Contributeurs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contributeurPopupRoute: Routes = [
    {
        path: 'contributeur-new',
        component: ContributeurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Contributeurs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contributeur/:id/edit',
        component: ContributeurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Contributeurs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contributeur/:id/delete',
        component: ContributeurDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Contributeurs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
