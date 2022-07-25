type getTokenRequest: void {
  .amount?: double
}

type getTokenResponse: void {
  .status: string
  .sid: string
 }

type verifyTokenRequest: void {
  .sid: string
}

type verifyTokenResponse: void {
  .success: bool
 }

 type refoundRequest: void {
  .sid: string
 }

 type refoundResponse: void {
  .success: bool
 }

interface interface {
    RequestResponse: getToken(getTokenRequest)(getTokenResponse)
    RequestResponse: verifyToken(verifyTokenRequest)(verifyTokenResponse)
    RequestResponse: refound(refoundRequest)(refoundResponse)
}