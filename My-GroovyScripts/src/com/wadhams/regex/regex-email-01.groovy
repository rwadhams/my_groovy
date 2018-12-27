import java.util.regex.Pattern

String input = '''
				<ns2:GeneralPartyInfo>
					<ns2:Communications>
						<ns2:PhoneInfo>
							<ns2:PhoneTypeCd>Phone</ns2:PhoneTypeCd>
							<ns2:CommunicationUseCd>Home</ns2:CommunicationUseCd>
							<ns2:CommunicationUseCd>Primary</ns2:CommunicationUseCd>
							<ns2:PhoneNumber>+1-201-555-4565</ns2:PhoneNumber>
						</ns2:PhoneInfo>
						<ns2:EmailInfo>
							<ns2:EmailAddr>tbasine@KEMPER.COM</ns2:EmailAddr>
							<ns2:DoNotContactInd>0</ns2:DoNotContactInd>
						</ns2:EmailInfo>
					</ns2:Communications>
				</ns2:GeneralPartyInfo>
'''
println input

Pattern emailPattern = ~/(?m)()/
Pattern emailPattern2 = ~/(?m)([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+)/
Pattern emailPattern3 = ~/(?m)(tbasine@KEMPER.COM)/

def findEmail = (input =~ emailPattern2)
if (findEmail && findEmail.hasGroup()) {
	println "Email: ${findEmail[0][1]}"
}



