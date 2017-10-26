import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Contributeur } from './contributeur.model';
import { ContributeurPopupService } from './contributeur-popup.service';
import { ContributeurService } from './contributeur.service';

@Component({
    selector: 'jhi-contributeur-delete-dialog',
    templateUrl: './contributeur-delete-dialog.component.html'
})
export class ContributeurDeleteDialogComponent {

    contributeur: Contributeur;

    constructor(
        private contributeurService: ContributeurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contributeurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contributeurListModification',
                content: 'Deleted an contributeur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contributeur-delete-popup',
    template: ''
})
export class ContributeurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contributeurPopupService: ContributeurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contributeurPopupService
                .open(ContributeurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
