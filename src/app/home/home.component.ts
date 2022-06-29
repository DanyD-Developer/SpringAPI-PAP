import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {pipe} from 'rxjs';
import {HttpClient, HttpEvent, HttpEventType, HttpHeaders, HttpResponse} from '@angular/common/http';
import {filter, map, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Toastr} from '../core/services/toastr/toastr';
import {ToastrFactory} from '../core/services/toastr/toastr.factory';
import {ModalsService} from '../core/services/modals.service';
import {InfoModalComponent} from 'src/app/core/components/info-modal/info-modal.component';

export function toResponseBody<T>(): any {
  return pipe(
    filter((event: HttpEvent<T>) => event.type === HttpEventType.Response),
    map((res: HttpResponse<T>) => res)
  );
}

export function uploadProgress<T>(cb: (progress: number) => void): any {
  return tap((event: HttpEvent<T>) => {
    if (event.type === HttpEventType.UploadProgress) {
      console.log(event);
      cb(Math.round((100 * event.loaded) / event.total));
    }
  });
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  progress = 0;
  form: FormGroup;

  constructor(private httpClient: HttpClient,
              private toastr: ToastrFactory,
              private modalsService: ModalsService) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.form = new FormGroup(
      {
        file: new FormControl(null, [Validators.required, this.requiredFileType('pdf')]),
        passwordsFile: new FormControl(null, [Validators.required, this.requiredFileType('json')])
      }
    );
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.checkControls(this.form);
      return;
    }


    const formValue = Object.assign({}, this.form.getRawValue());

    const data = {
      wageReceiptPdf: formValue.file,
      pwdFile: formValue.passwordsFile
    };

    const signData = this.toFormData(data);


    const url = `${environment.api}/wage-receipts/upload`

    this.httpClient.post(url, signData, {
      reportProgress: true,
      observe: 'events',
      responseType: 'json',
      headers: new HttpHeaders().set('accept', 'application/json'),
    }).pipe(
      uploadProgress(progress => (this.progress = progress)),
      toResponseBody()
    ).subscribe((response: HttpResponse<OwnerResponse[]>) => {

      let ownersList = response.body;

      this.modalsService.openModal(InfoModalComponent, {title: "Wage Receipts Split Result", list: ownersList});
      this.toastr.success(new Toastr(`Document(s) were uploaded successfully.`));

      this.form = null;

      setTimeout(() => {
        this.progress = 0;
        this.initForm();
      });
    }, response => {
      this.progress = 0;
      this.toastr.error(new Toastr('It wasn\'t possible to process the given files! Contact the support team.'));
    });
  }

  // ------------- Helper Functions ------------------ //

  toFormData(formValue: any): any {
    const formData = new FormData();

    for (const key of Object.keys(formValue)) {
      const value = formValue[key];
      formData.append(key, value);
    }

    return formData;
  }

  requiredFileType(type: string): ValidatorFn {
    return (control: FormControl) => {
      const file = control.value;
      if (file) {
        const splitString = file.name.split('.');
        const extension = splitString[splitString.length - 1].toLowerCase();
        if (type.toLowerCase() !== extension.toLowerCase()) {
          return {
            requiredFileType: true
          };
        }

        return null;
      }

      return null;
    };
  }

  public checkControls(control: any) {

    if (control.hasOwnProperty('controls') && Object.keys(control['controls']).length > 0) {
      Object.keys(control['controls']).forEach(key => {
        const innerControl = control.get(key);
        innerControl.markAsTouched();
        innerControl.markAsDirty();
        this.checkControls(innerControl);
      });
    }
    return;
  }

}

export interface OwnerResponse {
  name: string;
  message: string;
}
