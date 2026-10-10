import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnswerService, Answer } from '../../services/answer.service';
import { QUESTIONS } from '../../data/questions';

@Component({
  selector: 'app-result',
  imports: [CommonModule],
  templateUrl: './result.component.html',
  styleUrl: './result.component.css'
})
export class ResultComponent {

  answers: Answer[] = [];

  constructor(private answerService: AnswerService) {
    this.answers = this.answerService.getAnswers();
  }

  getQuestionText(questionId: number): string {
    const question = QUESTIONS.find(
      item => item.id === questionId
    );

    return question?.text ?? '';
  }

  getAnswerLabel(answer: Answer): string {
    const question = QUESTIONS.find(
      item => item.id === answer.questionId
    );

    if (!question?.choices) {
      return String(answer.answer);
    }

    const choice = question.choices.find(
      item => item.value === answer.answer
    );

    return choice?.label ?? String(answer.answer);
  }

  save(): void {
    this.answerService.saveToServer().subscribe({
      next: (response) => {
        console.log('サーバー保存成功:', response);
      },
      error: (error) => {
        console.error('サーバー保存失敗:', error);
      }
    });
  }
}