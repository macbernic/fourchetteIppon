import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Contributeur } from './contributeur.model';
import { ContributeurService } from './contributeur.service';

@Component({
    selector: 'jhi-contributeur-detail',
    templateUrl: './contributeur-detail.component.html'
})
export class ContributeurDetailComponent implements OnInit, OnDestroy {

    contributeur: Contributeur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contributeurService: ContributeurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContributeurs();
    }

    load(id) {
        this.contributeurService.find(id).subscribe((contributeur) => {
            this.contributeur = contributeur;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContributeurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contributeurListModification',
            (response) => this.load(this.contributeur.id)
        );
    }
}
