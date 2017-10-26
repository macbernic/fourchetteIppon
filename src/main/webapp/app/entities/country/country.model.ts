import { BaseEntity } from './../../shared';

export class Country implements BaseEntity {
    constructor(
        public id?: number,
        public countryName?: string,
    ) {
    }


    public getCountryNameById(id: number, countries: Country[]): Country {
        return countries.filter(c => c.id === id)[0];
    }
}
