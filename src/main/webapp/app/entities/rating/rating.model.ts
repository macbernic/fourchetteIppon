import { BaseEntity } from './../../shared';

export const enum FORK {
    'AVOID',
    'POOR',
    'ACCEPTABLE',
    'NICE',
    'GREAT'
}

export class Rating implements BaseEntity {
    constructor(
        public id?: number,
        public fork?: FORK,
        public comment?: string,
        public restaurantId?: number,
        public contributeurId?: number,
    ) {
    }
}
