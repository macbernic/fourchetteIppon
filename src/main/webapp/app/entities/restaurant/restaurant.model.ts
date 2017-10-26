import { BaseEntity } from './../../shared';

export class Restaurant implements BaseEntity {
    constructor(
        public id?: number,
        public restaurantName?: string,
        public locationId?: number,
        public ratings?: BaseEntity[],
    ) {
    }
}
