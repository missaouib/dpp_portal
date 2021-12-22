// <!--begin::Global Config-->
var HOST_URL =
  "https://preview.keenthemes.com/metronic/theme/html/tools/preview";
// <!--begin::Global Theme Bundle(used by all pages)-->
var KTAppSettings = {
  breakpoints: { sm: 576, md: 768, lg: 992, xl: 1200, xxl: 1400 },
  colors: {
    theme: {
      base: {
        white: "#ffffff",
        primary: "#3699FF",
        secondary: "#E5EAEE",
        success: "#1BC5BD",
        info: "#8950FC",
        danger: "#F64E60",
        light: "#E4E6EF",
        dark: "#181C32",
      },
      light: {
        warning: "#FFA800",
        white: "#ffffff",
        primary: "#E1F0FF",
        secondary: "#EBEDF3",
        success: "#C9F7F5",
        info: "#EEE5FF",
        warning: "#FFF4DE",
        danger: "#FFE2E5",
        light: "#F3F6F9",
        dark: "#D6D6E0",
      },
      inverse: {
        white: "#ffffff",
        primary: "#ffffff",
        secondary: "#3F4254",
        success: "#ffffff",
        info: "#ffffff",
        warning: "#ffffff",
        danger: "#ffffff",
        light: "#464E5F",
        dark: "#ffffff",
      },
    },
    gray: {
      "gray-100": "#F3F6F9",
      "gray-200": "#EBEDF3",
      "gray-300": "#E4E6EF",
      "gray-400": "#D1D3E0",
      "gray-500": "#B5B5C3",
      "gray-600": "#7E8299",
      "gray-700": "#5E6278",
      "gray-800": "#3F4254",
      "gray-900": "#181C32",
    },
  },
  "font-family": "Poppins",
};
// <!--end::Global Theme Bundle-->
// <!--end::Global Config-->

(function ($) {
  $.markingErrorField = function (response) {
    const errorFields = response.responseJSON.errors;

    if (!errorFields) {
      $("#errMessage").html(
        '<div class="alert alert-danger">' +
          response.responseJSON.message +
          "</div>"
      );
      return;
    }

    var $field, error;
    for (var i = 0, length = errorFields.length; i < length; i++) {
      error = errorFields[i];
      $field = $("#" + error["field"]);

      if ($field && $field.length > 0) {
        $field.siblings(".error-message").remove();
        $field.after(
          '<span class="error-message text-small text-danger">' +
            error.defaultMessage +
            "</span>"
        );
      }
    }
  };

  $.blockPage = {
    init: {
      color: "#ffffff",
      state: "primary",
      message: "처리중입니다...",
    },

    full: function () {
      KTApp.blockPage({
        overlayColor: "#000000",
        state: "primary",
        message: "처리중입니다...",
      });
    },

    single: function (id) {
      KTApp.block("#" + id, {
        overlayColor: "#ffffff",
        state: "primary",
        message: "처리중입니다...",
      });
    },

    remove: function (timeout = 0) {
      setTimeout(function () {
        KTApp.unblockPage();
      }, timeout);
    },
  };
})(jQuery);
