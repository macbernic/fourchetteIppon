import { BaseEntity } from './../../shared';

export class Contributeur implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public creationDate?: any,
        public ratings?: BaseEntity[],
    ) {
    }
}
