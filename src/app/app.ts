import { Component, signal } from '@angular/core';
import { QuestionComponent } from './components/question/question.component';

@Component({
  imports: [QuestionComponent],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('mental-web');
}