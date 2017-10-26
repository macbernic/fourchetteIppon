import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FourchetteIpponSharedModule } from '../../shared';
import {
    ContributeurService,
    ContributeurPopupService,
    ContributeurComponent,
    ContributeurDetailComponent,
    ContributeurDialogComponent,
    ContributeurPopupComponent,
    ContributeurDeletePopupComponent,
    ContributeurDeleteDialogComponent,
    contributeurRoute,
    contributeurPopupRoute,
} from './';

const ENTITY_STATES = [
    ...contributeurRoute,
    ...contributeurPopupRoute,
];

@NgModule({
    imports: [
        FourchetteIpponSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ContributeurComponent,
        ContributeurDetailComponent,
        ContributeurDialogComponent,
        ContributeurDeleteDialogComponent,
        ContributeurPopupComponent,
        ContributeurDeletePopupComponent,
    ],
    entryComponents: [
        ContributeurComponent,
        ContributeurDialogComponent,
        ContributeurPopupComponent,
        ContributeurDeleteDialogComponent,
        ContributeurDeletePopupComponent,
    ],
    providers: [
        ContributeurService,
        ContributeurPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FourchetteIpponContributeurModule {}
