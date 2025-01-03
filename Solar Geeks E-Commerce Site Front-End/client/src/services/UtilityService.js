export default {
    formatCurrency(monetaryAmount, currencyFormat = 'USD', decimalPlaces = 2) {
        return new Intl.NumberFormat('en-US', { style: 'currency', currency: currencyFormat, maximumFractionDigits: decimalPlaces }).format(monetaryAmount);
    },
    errorHandler(error) {
        if(error.response) {
            if(error.response.status === 404) {
                alert('Not found.');
            } else {
                alert('System issue, please try again.')
            }
        } else if (error.request) {
            alert('Server could not be reached.')
        } else {
            alert('Request could not be created.')
        }
    },
}