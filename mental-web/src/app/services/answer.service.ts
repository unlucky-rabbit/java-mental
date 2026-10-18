import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Answer {
  questionId: number;
  answer: number | string;
}

@Injectable({
  providedIn: 'root'
})
export class AnswerService {

  private answers: Answer[] = [];

  constructor(private http: HttpClient) {}

  saveAnswer(questionId: number, answer: number | string): void {
    const existingAnswer = this.answers.find(
      item => item.questionId === questionId
    );

    if (existingAnswer) {
      existingAnswer.answer = answer;
      return;
    }

    this.answers.push({
      questionId,
      answer
    });
  }

  getAnswers(): Answer[] {
    return [...this.answers];
  }

  getAnswer(questionId: number): Answer | undefined {
    return this.answers.find(
      item => item.questionId === questionId
    );
  }

  clearAnswers(): void {
    this.answers = [];
  }

  saveToServer() {
    return this.http.post(
      'http://localhost:8080/mental-api/api/results',
      {
        answers: this.answers
      }
    );
  }
}