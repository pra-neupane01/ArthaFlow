function validateTransactionForm(form) {
    let amount = form.amount.value;
    if (amount === "" || isNaN(amount) || parseFloat(amount) <= 0) {
        alert("Please enter a valid positive amount.");
        return false;
    }
    return true;
}