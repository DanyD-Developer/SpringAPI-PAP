import {ToastrMessage} from './toastr-message.model';

export class Toastr {

  constructor(message: string, translationPlaceholders = {}) {
    this._body = new ToastrMessage(message, translationPlaceholders);
  }

  private _title: ToastrMessage;

  get title(): ToastrMessage {
    return this._title;
  }

  private _body: ToastrMessage;

  get body(): ToastrMessage {
    return this._body;
  }

  private _detail: string;

  get detail(): string {
    return this._detail;
  }

  setTitle(title: string, translationPlaceholders = {}): Toastr {
    this._title = new ToastrMessage(title, translationPlaceholders);
    return this;
  }

  setDetail(detail: string): Toastr {
    this._detail = detail;
    return this;
  }
}
