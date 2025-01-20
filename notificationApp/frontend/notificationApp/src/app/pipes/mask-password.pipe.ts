import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'maskPassword'
})
export class MaskPasswordPipe implements PipeTransform {

  transform(value: string, maskChar: string = '*'): string {
    if(!value) return '';
    return maskChar.repeat(8);
  }

}
