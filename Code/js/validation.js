// Create a new instance of JustValidate and specify the form element to validate with the ID "#signup"
const validation = new JustValidate("#signup");

// Add validation rules for each field in the form
validation
  // Add validation for the first name field
  .addField("#first_name", [
    {
      rule: "required", // Require the field to be filled
      errorMessage: "First name is required", // Error message to display if the field is not filled
    },
  ])
  // Add validation for the second name field
  .addField("#second_name", [
    {
      rule: "required", // Require the field to be filled
      errorMessage: "Second name is required", // Error message to display if the field is not filled
    },
  ])
  // Add validation for the email address field
  .addField("#email_address", [
    {
      rule: "required", // Require the field to be filled
      errorMessage: "Email address is required", // Error message to display if the field is not filled
    },
    {
      rule: "email", // Check if the value is a valid email address
      errorMessage: "Invalid email address", // Error message to display if the email address is invalid
    },
    {
      // Custom validator to check if the email address is already taken
      validator: (value) => {
        return fetch("validate-email.php?email=" + encodeURIComponent(value))
          .then(function (response) {
            return response.json();
          })
          .then(function (json) {
            return json.available;
          });
      },
      errorMessage: "Email already taken", // Error message to display if the email address is already taken
    },
  ])
  // Add validation for the password field
  .addField("#password", [
    {
      rule: "required", // Require the field to be filled
      errorMessage: "Password is required", // Error message to display if the field is not filled
    },
    {
      rule: "password", // Validate the password format (custom rule)
      errorMessage: "Invalid password", // Error message to display if the password format is invalid
    },
  ])
  // Add validation for the password confirmation field
  .addField("#password_confirmation", [
    {
      // Custom validator to check if the password confirmation matches the password
      validator: (value, fields) => {
        return value === fields["#password"].elem.value; // Compare the value with the value of the password field
      },
      errorMessage: "Passwords should match", // Error message to display if the passwords do not match
    },
  ])
  // Execute the following function when the form validation is successful
  .onSuccess((event) => {
    document.getElementById("signup").submit(); // Submit the form
  });
