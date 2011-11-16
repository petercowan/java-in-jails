/**
 * Returns true if the given credit card number passes the Luhn algorithm test.
 * A true response does not mean the card itself is valid, only that the card
 * number is a in a format that proves that card could potentially be valid.
 */
function isValidLuhnNumber(cardNumber) {
	cardNumber = cardNumber.replace(/ +/g, '');
	cardNumber = cardNumber.replace(/-/g, '');
	var numDigits = cardNumber.length;
//	alert('numDigits ' + numDigits);
	if (numDigits >= 14 && numDigits <= 16 && parseInt(cardNumber) > 0) {
		var i, pos = 1, digit, doubleDigits = new String();

		for (i = numDigits - 1; i >= 0; i--) {
			digit = parseInt(cardNumber.charAt(i));
			if (pos++ % 2 == 0) doubleDigits += digit * 2;
			else doubleDigits += digit;
		}
//		alert('doubleDigits ' + doubleDigits);

		var sum = 0;
//		alert('doubleDigits.length ' + doubleDigits.length);
		for (i = 0; i < doubleDigits.length; i++) {
			digit = parseInt(doubleDigits.charAt(i));
			sum += digit;
		}
//		alert('sum ' + sum);
		return (sum % 10 == 0);
	}
	return false;
}

function checkCreditCard(field, rules, i, options) {
	if (!isValidLuhnNumber(field.val())) {
		// this allows the use of i18 for the error msgs
		return '* Not a valid Credit Card Number';
	}
}
