type loginRequest: void {
  .username: string
  .password: string
  .bill?: double
  .order_id?: int
  .to_user?: int
}

type loginResponse: void {
  .sid?: string
  .url?: string
  .success: bool
 }
 
type payRequest: void{
	.sid: string
}

type payResponse: void{
	.success: bool
	.token?: string
}

type logoutRequest: void{
	.sid: string
}

type verifyTokenRequest: void{
	.sid: string
	.token: string
	.order_id: int
}

type successResponse: void{
	.success: bool
}

type refoundRequest: void{
	.sid: string
	.order_id: int
}

type confirmRequest: void{
	.sid: string
	.order_id: int
}

interface interface {
    RequestResponse:
		login(loginRequest)(loginResponse),
		verifyToken(verifyTokenRequest)(successResponse),
		pay(payRequest)(payResponse),
		logout(logoutRequest)(successResponse),
		refound(refoundRequest)(successResponse),
		confirm(confirmRequest)(successResponse)
}