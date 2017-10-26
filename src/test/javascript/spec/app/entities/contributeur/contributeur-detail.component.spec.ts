/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FourchetteIpponTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ContributeurDetailComponent } from '../../../../../../main/webapp/app/entities/contributeur/contributeur-detail.component';
import { ContributeurService } from '../../../../../../main/webapp/app/entities/contributeur/contributeur.service';
import { Contributeur } from '../../../../../../main/webapp/app/entities/contributeur/contributeur.model';

describe('Component Tests', () => {

    describe('Contributeur Management Detail Component', () => {
        let comp: ContributeurDetailComponent;
        let fixture: ComponentFixture<ContributeurDetailComponent>;
        let service: ContributeurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FourchetteIpponTestModule],
                declarations: [ContributeurDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ContributeurService,
                    JhiEventManager
                ]
            }).overrideTemplate(ContributeurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContributeurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContributeurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Contributeur(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.contributeur).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
