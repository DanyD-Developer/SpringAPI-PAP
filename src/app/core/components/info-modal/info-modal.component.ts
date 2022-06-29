import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {OwnerResponse} from 'src/app/home/home.component';


@Component({
  selector: 'app-info-modal',
  templateUrl: './info-modal.component.html'
})
export class InfoModalComponent implements OnInit {

  @Input() data: { title: string, list: OwnerResponse[] };

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit() {

  }

  onSubmit() {
    this.activeModal.close();
  }
}
