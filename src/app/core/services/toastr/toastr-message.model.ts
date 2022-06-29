export class ToastrMessage {

  constructor(message: string, translationPlaceholders = {}) {
    this._message = message;
    this._messagePlaceholders = translationPlaceholders;
  }

  private _message: string;

  get message(): string {
    return this._message;
  }

  private _messagePlaceholders: any;

  get messagePlaceholders(): any {
    return this._messagePlaceholders;
  }


}
