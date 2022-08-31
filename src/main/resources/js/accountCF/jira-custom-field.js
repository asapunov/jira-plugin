window.onload = function() {
    AJS.$('[data-tooltip]').tooltip({
        title: function () {
            return this.getAttribute("data-tooltip");
        },
        gravity: 'w'
    });
};
