import {Injectable} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class ModalsService {

  constructor(private modalService: NgbModal) {
  }

  openModal(component: any, modalData: any, properties?: any) {
    const modal = this.modalService.open(component, properties);
    modal.componentInstance['data'] = modalData;
    return modal;
  }
}
