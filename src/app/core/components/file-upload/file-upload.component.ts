import {Component, DoCheck, ElementRef, Input, Renderer2, TemplateRef, ViewChild} from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  // providers: [
  //   {
  //     provide: NG_VALUE_ACCESSOR,
  //     useExisting: FileUploadComponent,
  //     multi: true
  //   }
  // ]
})
export class FileUploadComponent implements ControlValueAccessor, DoCheck {

  @Input() template: TemplateRef<any>;
  @Input() progress;
  @Input() extension: string;
  onChange: Function;
  onTouched: Function;
  file: File | null = null;
  touched = false;

  @ViewChild('fileInput') fileInput: ElementRef;


  selectFile(event): any {
    const file = event && (event.target.files as FileList).item(0);
    this.onTouched();
    this.onChange(file);
    this.file = file;
    this.checkValidity();
  }

  constructor(private controlDirective: NgControl, private host: ElementRef<HTMLInputElement>, private renderer: Renderer2) {
    this.controlDirective.valueAccessor = this;
  }

  ngDoCheck(): void {
    if (this.touched !== this.controlDirective.control.touched) {
      this.touched = this.controlDirective.control.touched;
      if (this.touched) {
        this.checkValidity();
      }
    }
  }

  checkValidity(): void {
    if (!!this.fileInput && !!this.controlDirective.control && this.controlDirective.control.touched) {
      if (this.controlDirective.control.invalid) {
        this.renderer.addClass(this.fileInput.nativeElement, 'ng-invalid');
        this.renderer.addClass(this.fileInput.nativeElement, 'ng-touched');
      } else {
        this.renderer.removeClass(this.fileInput.nativeElement, 'ng-invalid');
        this.renderer.removeClass(this.fileInput.nativeElement, 'ng-touched');
      }
    }

  }

  click() {
    this.controlDirective.control.markAsTouched();
  }

  input() {
    this.checkValidity();
  }


  writeValue(value: null): void {
    // clear file input
    this.host.nativeElement.value = '';
    this.file = null;
  }

  registerOnChange(fn: Function): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: Function): void {
    this.onTouched = fn;
  }


}
