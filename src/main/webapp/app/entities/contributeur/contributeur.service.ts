import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Contributeur } from './contributeur.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ContributeurService {

    private resourceUrl = SERVER_API_URL + 'api/contributeurs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(contributeur: Contributeur): Observable<Contributeur> {
        const copy = this.convert(contributeur);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(contributeur: Contributeur): Observable<Contributeur> {
        const copy = this.convert(contributeur);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Contributeur> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Contributeur.
     */
    private convertItemFromServer(json: any): Contributeur {
        const entity: Contributeur = Object.assign(new Contributeur(), json);
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(json.creationDate);
        return entity;
    }

    /**
     * Convert a Contributeur to a JSON which can be sent to the server.
     */
    private convert(contributeur: Contributeur): Contributeur {
        const copy: Contributeur = Object.assign({}, contributeur);

        copy.creationDate = this.dateUtils.toDate(contributeur.creationDate);
        return copy;
    }
}
