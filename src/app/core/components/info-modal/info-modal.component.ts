import {Component, Input, OnInit, Output} from '@angular/core';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { OwnerResponse, OwnersListResponse } from 'src/app/home/home.component';


@Component({
  selector: 'app-info-modal',
  templateUrl: './info-modal.component.html'
})
export class InfoModalComponent implements OnInit {

  @Input() data: { title: string, list: Array<OwnerResponse> };

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit() {

  }

  onSubmit() {
    this.activeModal.close();
  }
}
