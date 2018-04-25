<template>
    <div>
        <slot>
        </slot>
        <br>
        <div class="date-time-picker">
            <div>
                <label :for="idD">Date</label>
                <input :id="idD" type="text" class="datepicker">
            </div>
            <div>
                <label :for="idT">Time</label>
                <input :id="idT" type="text" class="timepicker">
            </div>
        </div>
    </div>
</template>
<script>
import { stringUtil, datetime } from '../utils';

export default {
    name: 'app-date-time-picker',
    data: () => ({
        idT: null,
        idD: null,
    }),
    methods: {
    },
    components: {
        
    },
    mounted() {
        const self = this;

        this.idT = stringUtil.generateID();
        this.idD = stringUtil.generateID();
        this.$nextTick(() => {
            
            const dtp = $(`.datepicker#${this.idD}`);
            const tmp = $(`.timepicker#${this.idT}`);
            const handleSet = (c) => {
                if(dtp.val().length > 0 && tmp.val().length > 0) {
                    self.$emit('onPicked', dtp.pickadate('picker').get('select').obj.getTime()
                        + datetime.miliseconds.apply(null, tmp.val().split(':')));
                }
            }


            dtp.pickadate({
                selectMonths: true, // Creates a dropdown to control month
                selectYears: 15, // Creates a dropdown of 15 years to control year,
                today: 'Today',
                clear: 'Clear',
                close: 'Ok',
                closeOnSelect: false, // Close upon selecting a date,
                onSet: handleSet,
            });
            tmp.pickatime({
                default: 'now', // Set default time: 'now', '1:30AM', '16:30'
                fromnow: 0,       // set default time to * milliseconds from now (using with default = 'now')
                twelvehour: false, // Use AM/PM or 24-hour format
                donetext: 'OK', // text for done-button
                cleartext: 'Clear', // text for clear-button
                canceltext: 'Cancel', // Text for cancel-button
                autoclose: false, // automatic close timepicker
                ampmclickable: true, // make AM PM clickable
                aftershow: function(){}, //Function for after opening timepicker
            });

            tmp.on('change', (e) => {
                self.time
                handleSet();
            });
        });
    },
}
</script>
<style>
    .date-time-picker {

    }
</style>