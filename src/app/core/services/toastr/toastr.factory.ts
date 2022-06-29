import {Injectable} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {ToastrMessage} from './toastr-message.model';
import {Toastr} from './toastr';

@Injectable()
export class ToastrFactory {

  private _config = {enableHtml: true};

  constructor(private toastrService: ToastrService) {
  }

  success(toastr: Toastr, config = {}) {
    this.toastrService.success(
      this.translateMessages(toastr.body, toastr.detail), this.translateMessages(toastr.title), Object.assign(this._config, config));
  }

  error(toastr: Toastr, config = {}) {
    this.toastrService.error(
      this.translateMessages(toastr.body, toastr.detail), this.translateMessages(toastr.title), Object.assign(this._config, config));
  }

  warning(toastr: Toastr, config = {}) {
    this.toastrService.warning(
      this.translateMessages(toastr.body, toastr.detail), this.translateMessages(toastr.title), Object.assign(this._config, config));
  }

  info(toastr: Toastr, config = {}) {
    this.toastrService.info(
      this.translateMessages(toastr.body, toastr.detail), this.translateMessages(toastr.title), Object.assign(this._config, config));
  }

  show(toastr: Toastr, config = {}) {
    this.toastrService.show(
      this.translateMessages(toastr.body, toastr.detail), this.translateMessages(toastr.title), Object.assign(this._config, config));
  }

  translateMessages(message: ToastrMessage, detail?: string): string {

    detail = detail ? '<hr><b>Detail:</b><br>' + detail : '';

    return message && message.message ? message.message + detail : '';
  }

}
