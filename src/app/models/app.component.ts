import { Component } from '@angular/core';
import { QuestionComponent } from '../components/question/question.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [QuestionComponent],
  template: `
    <app-question>mental health questionnaire</app-question>
  `
})
export class AppComponent {
}