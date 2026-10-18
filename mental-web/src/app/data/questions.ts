import { Question } from '../models/question.model';

export const QUESTIONS: Question[] = [
  {
    id: 1,
    text: '最近、よく眠れていますか？',
    type: 'single-choice',
    choices: [
      { value: 1, label: 'はい' },
      { value: 2, label: 'どちらともいえない' },
      { value: 3, label: 'いいえ' }
    ]
  },

  {
    id: 2,
    text: '仕事や勉強に集中できていますか？',
    type: 'single-choice',
    choices: [
      { value: 1, label: 'はい' },
      { value: 2, label: 'どちらともいえない' },
      { value: 3, label: 'いいえ' }
    ]
  },

  {
    id: 3,
    text: '理由もなく不安になることがありますか？',
    type: 'single-choice',
    choices: [
      { value: 1, label: 'はい' },
      { value: 2, label: 'どちらともいえない' },
      { value: 3, label: 'いいえ' }
    ]
  }
];