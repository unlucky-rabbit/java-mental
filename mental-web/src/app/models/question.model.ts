export type QuestionType =
  | 'single-choice'
  | 'scale'
  | 'multiple-choice'
  | 'text';

export interface QuestionChoice {
  value: number | string;
  label: string;
}

export interface Question {
  id: number;
  text: string;
  type: QuestionType;
  choices?: QuestionChoice[];
}