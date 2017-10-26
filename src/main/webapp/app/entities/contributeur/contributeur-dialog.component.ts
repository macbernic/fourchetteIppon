import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Contributeur } from './contributeur.model';
import { ContributeurPopupService } from './contributeur-popup.service';
import { ContributeurService } from './contributeur.service';

@Component({
    selector: 'jhi-contributeur-dialog',
    templateUrl: './contributeur-dialog.component.html'
})
export class ContributeurDialogComponent implements OnInit {

    contributeur: Contributeur;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private contributeurService: ContributeurService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contributeur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contributeurService.update(this.contributeur));
        } else {
            this.subscribeToSaveResponse(
                this.contributeurService.create(this.contributeur));
        }
    }

    private subscribeToSaveResponse(result: Observable<Contributeur>) {
        result.subscribe((res: Contributeur) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Contributeur) {
        this.eventManager.broadcast({ name: 'contributeurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-contributeur-popup',
    template: ''
})
export class ContributeurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contributeurPopupService: ContributeurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contributeurPopupService
                    .open(ContributeurDialogComponent as Component, params['id']);
            } else {
                this.contributeurPopupService
                    .open(ContributeurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
