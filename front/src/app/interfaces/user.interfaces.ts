export interface User {
	id: number,
	user_name: string,
	email: string,
	password?:String,
	created_at: Date,
	updated_at: Date
}