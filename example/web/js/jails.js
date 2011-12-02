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

function _creditCard(field) {
    var valid = false, cardNumber = field.replace(/ +/g, '').replace(/-+/g, '');

    var numDigits = cardNumber.length;
    if (numDigits >= 14 && numDigits <= 16 && parseInt(cardNumber) > 0) {

        var sum = 0, i = numDigits - 1, pos = 1, digit, luhn = new String();
        do {
            digit = parseInt(cardNumber.charAt(i));
            luhn += (pos++ % 2 == 0) ? digit * 2 : digit;
        } while (--i >= 0)

        for (i = 0; i < luhn.length; i++) {
            sum += parseInt(luhn.charAt(i));
        }
        valid = sum % 10 == 0;
    }
    if (!valid) return "Please enter a valid credit card number";
}

/*
function dateHelper(dateId) {
	var el = $('#' + dateId);
	var input = $('input', el);
	var output = $('.' +dateId + 'output', el);
	input.keyup(function() {
		var val = input.val().trim();
		if (/^\d+$/.test(val)) {
			val = val.toNumber();
		}
		var text, date = Date.create(val);
		if (!date.isValid()) {
			text = 'Invalid date.'
		} else {
			text = date.format('{Weekday} {Month} {ord}, {year} {HH}:{mm}:{ss}');
			var hiddenField = date.format('{MM}-{dd}-{yyyy}');
		}
		output.text(text);
	});
}
  */
//$(document).ready(dateHelper('user.dateCreated'));