import {Question} from "./question";
import {Book} from "../book/book";
import {User} from "../user/user";
import {Answer} from "./answer";

export interface QuestionWithData extends Question {
	bookInfo: Partial<Book>,
	writerInfo?: Partial<User>
	questionerInfo?: Partial<User>
	answerInfo?: Partial<Answer>
}