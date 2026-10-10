import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Question } from '../../models/question.model';
import { QUESTIONS } from '../../data/questions';
import { AnswerService } from '../../services/answer.service';
import { ResultComponent } from '../result/result.component';

@Component({
  selector: 'app-question',
  imports: [CommonModule, ResultComponent],
  templateUrl: './question.component.html',
  styleUrl: './question.component.css'
})
export class QuestionComponent {

  questions: Question[] = QUESTIONS;

  currentIndex = 0;

  selectedAnswer: number | string | null = null;

  showResult = false;

  constructor(private answerService: AnswerService) {}

  get currentQuestion(): Question {
    return this.questions[this.currentIndex];
  }

  get isLastQuestion(): boolean {
    return this.currentIndex === this.questions.length - 1;
  }

  selectAnswer(value: number | string): void {
    this.selectedAnswer = value;
  }

  nextQuestion(): void {

    if (this.selectedAnswer === null) {
      return;
    }

    this.answerService.saveAnswer(
      this.currentQuestion.id,
      this.selectedAnswer
    );

    if (!this.isLastQuestion) {

      this.currentIndex++;
      this.selectedAnswer = null;

    } else {

      this.showResult = true;

    }
  }
}