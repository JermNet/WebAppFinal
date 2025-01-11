
// All of this is so shifts can't be created for dates/times before the current date, and so the endTime of a shift can't take place before the starTime
document.addEventListener('DOMContentLoaded', function () {
    // Get all date, start time, and end time inputs by class
    const dateInputs = document.querySelectorAll('.date');
    const startTimeInputs = document.querySelectorAll('.start-time');
    const endTimeInputs = document.querySelectorAll('.end-time');

    // Get the current date and time
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const minDate = `${year}-${month}-${day}`;

    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const minTime = `${hours}:${minutes}`;

    // Function to handle changes for a specific group of date, start time, and end time
    function setUpListeners(dateInput, startTimeInput, endTimeInput) {
        dateInput.min = minDate;

        dateInput.addEventListener('change', function () {
            if (dateInput.value === minDate) {
                startTimeInput.min = minTime;
                endTimeInput.min = minTime;
            } else {
                startTimeInput.min = "00:00";
                endTimeInput.min = "00:00";
            }
        });

        startTimeInput.addEventListener('input', function () {
            if (dateInput.value === minDate && startTimeInput.value < minTime) {
                startTimeInput.value = minTime;
            }
            endTimeInput.min = startTimeInput.value;
            if (endTimeInput.value && endTimeInput.value < startTimeInput.value) {
                endTimeInput.value = startTimeInput.value;
            }
        });

        endTimeInput.addEventListener('input', function () {
            if (dateInput.value === minDate && endTimeInput.value < minTime) {
                endTimeInput.value = minTime;
            }
            if (endTimeInput.value < startTimeInput.value) {
                endTimeInput.value = startTimeInput.value;
            }
        });
    }

    // Set up listeners for each group of inputs
    dateInputs.forEach((dateInput, index) => {
        const startTimeInput = startTimeInputs[index];
        const endTimeInput = endTimeInputs[index];
        if (startTimeInput && endTimeInput) {
            setUpListeners(dateInput, startTimeInput, endTimeInput);
        }
    });
});

// Use this to check if passwords are the same
function validatePassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const mismatchWarning = document.getElementById('password-mismatch');

    if (password !== confirmPassword) {
        mismatchWarning.style.display = 'block';
        return false;
    }
    mismatchWarning.style.display = 'none';
    return true;
}