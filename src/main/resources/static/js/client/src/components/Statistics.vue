<template>
    <div>
        <div v-if="!selectedFeedbackRound">
            <div class="feedback-rounds" v-for="fr in feedbackRounds" v-if="fr.startDate < new Date()" @click="selectedFeedbackRound = fr">
                <b>{{ fr.manager.firstName }} {{ fr.manager.surname }}</b><br>
                <small>Started {{ fr.startDate }}<br><span :style="'color: ' + (fr.finished ? 'red' : 'green') + ';'" >{{ fr.finished ? 'already ended' : '...still running' }}</span></small>
            </div>
        </div>

        <div v-if="selectedFeedbackRound">
            <div v-if="selectedFeedbackRound.finished">
                <b @click="showStatistics = !showStatistics" style="cursor: pointer;color: #a20000;">Display Results</b>
                <div v-if="showStatistics" v-for="(stat,cat) in frCategoryStats(selectedFeedbackRound)">
                    <b @click="currentCategory = currentCategory === cat ? '' : cat" style="cursor: pointer;"><i>{{ cat }}: </i></b><span>{{ Math.round(100 * stat.result / stat.max) }}%</span><br>
                    <div class="tooltipped" :data-tooltip="stat.result + ' / ' + stat.max" style="position: relative; height: 5px; display: block; width: 100%; background-color: #f9cbe2; border-radius: 2px; margin: 0.5rem 0 1rem 0;overflow: hidden">
                        <div :style="'position: absolute; top: 0; left: 0; bottom: 0; background-color: #e20074; -webkit-transition: width .3s linear;transition: width .3s linear; width: ' + Math.round(100 * stat.result / stat.max) + '%'">
                        </div>
                    </div>
                    <div v-if="currentCategory === cat" v-for="(qstat,qt) in frQuestionsStats(selectedFeedbackRound, cat)" style="margin-left: 20px;">
                        <i>{{ qt }}: </i><span>{{ Math.round(100 * qstat.result / qstat.max) }}%</span><br>
                        <div class="tooltipped" :data-tooltip="qstat.result + ' / ' + qstat.max" style="position: relative; height: 5px; display: block; width: 100%; background-color: #fff59c; border-radius: 2px; margin: 0.5rem 0 1rem 0;overflow: hidden">
                            <div :style="'position: absolute; top: 0; left: 0; bottom: 0; background-color: #ead511; -webkit-transition: width .3s linear;transition: width .3s linear; width: ' + Math.round(100 * qstat.result / qstat.max) + '%'">
                            </div>
                        </div>                        
                    </div>
                </div>
            </div>
            <div v-else>
                <b @click="endRound">End feedback round</b><br>
                <b @click="sendReminders">Send reminders</b>
            </div>
            <br>
            <br>
            <button @click="selectedFeedbackRound = null; showStatistics = false;" class="btn waves-effect waves-light red lighten-1">
                Go Back
            </button>
        </div>

        <br>
        <div>
            <button @click="downloadPDF()" class="btn waves-effect waves-light green lighten-1">
                {{ pdf ? 'Download' : 'PDF Report' }}
            </button>
        </div>
        <br>

        <div id="pdfContainer" v-if="pdf">
            <div style="margin: 3% 29% 3% 29%">
                <!-- PAGE HEADER -->
                <div style="height: 58px; margin: 0;  position: relative;">
                    <div style="bottom: 0; display: inline; left: 0; position: absolute;"><img src="/images/pdf_fibimeter_logo.png"></div>
                    <img style="float: left; position: absolute; height: 30px; display: inline; bottom: 16px; right: 0px;" src="/images/logo_tsystem.svg">
                </div>
                <h1 style="color: #444242; font-size: 45px"><center>Summary Of Competencies</center></h1>

                <div v-for="(stat, mngr) in frManagersStats(feedbackRounds)">                
                    <!-- MANAGER HEADER -->
                    <h3 style="color: #505050; font-size: 25px; padding-left: 20px; color:#7d7a7a">Feedback report for {{mngr}}</h3>
                    <!--Meno Juraj carnogursky sa bude dynamicky nahradzovat -->

                    <!-- MANAGER BODY ruzovy ohraniceny obdlznik -->
                    <div style="overflow: hidden; min-height: 90px; border: 2px solid rgb(226,0,116); padding: 10px 10px; border-radius:7px; margin: 0; position: relative   ">

                        <div v-for="(catStat, cat) in stat.categories">                            
                            <!-- CATEGORY HEADER -->
                            <div style="float: left; width: 60%; display: inline; ">
                                <div style="padding-bottom: 7px; color:#444242"><strong><em>{{cat}}: </em></strong><span>{{ Math.round(100 * catStat.result / catStat.max) }}%</span></div>
                                <!-- The ability to contribute to teams and to improve ther effectiveness though personal commitment. -->
                            </div>
                            <!-- CATEGORY PROGRESS-BAR -->
                            <div style="float: left; display: inline; width: 40%; bottom: 0; right: 10px; margin: 0 auto;">
                                <!-- <b style="color:#444242">Your self score:</b> -->
                                <div style="position: relative; height: 5px; display: block; width: 155%; background-color: #f9cbe2; border-radius: 2px; margin: 0.5rem 0 1rem -80px;overflow: hidden">
                                    <div :style="'position: absolute; top: 0; left: 0; bottom: 0; background-color: #e20074; -webkit-transition: width .3s linear;transition: width .3s linear; width: ' + Math.round(100 * catStat.result / catStat.max) + '%'">
                                        <!-- with je progress ciary -->
                                    </div>
                                </div>
                                <!--
                                <b style="color:#444242">Feedback score:</b>
                                <div style="position: relative; height: 5px; display: block; width: 100%; background-color: #fff59c; border-radius: 2px; margin: 0.5rem 0 1rem 0;overflow: hidden">
                                    <div style=" position: absolute; top: 0; left: 0; bottom: 0; background-color: #ead511; -webkit-transition: width .3s linear;transition: width .3s linear; width: 70%">
                                    </div>
                                </div>
                                -->
                            </div>
                            <!-- CATEGORY QUESTIONS -->
                            <div v-for="(qstat,qt) in stat.questions" v-if="qstat.category === cat" style="margin-left: 20px;width: 100%">
                                <i style="float: left; margin-left: -40px;">{{ qt }}:&nbsp</i><span>{{ Math.round(100 * qstat.result / qstat.max) }}%</span><br>
                                <div :title="qstat.result + ' / ' + qstat.max" style="position: relative; height: 5px; display: block; width: 50%; background-color: #fff59c; border-radius: 2px; margin: 0.5rem 0 1rem 157px;overflow: hidden">
                                    <div :style="'position: absolute; top: 0; left: 0; bottom: 0; background-color: #ead511; -webkit-transition: width .3s linear;transition: width .3s linear; width: ' + Math.round(100 * qstat.result / qstat.max) + '%'">
                                    </div>
                                </div>                        
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
</template>
<script>
import ajax from '../ajax';

export default {
    name: 'app-statistics',
    data: () => ({
        selectedFeedbackRound: null,
        showStatistics: false,
        currentCategory: '',
        pdf: false,
        feedbackRounds: [
            {
                feedbackRoundId: 12,
                startDate: new Date(Date.now() - 120000),
                finished: false,
                manager: {
                    firstName: 'Jozef',
                    surname: 'Buk',
                },
                feedbacks: [
                    {
                        question: {
                            title: 'Bol dobry cely rok',
                            category: 'Motivation',
                        },
                        answer: 1,
                    },
                    {
                        question: {
                            title: 'Bol poslusny cely rok',
                            category: 'Team Work',
                        },
                        answer: 4,
                    },
                    {
                        question: {
                            title: 'Bol pracovity cely rok',
                            category: 'Reliability',
                        },
                        answer: 2,
                    },
                ],
            },
            {
                feedbackRoundId: 5,
                startDate: new Date(Date.now() - (15 * 24 * 60 * 60000)),
                finished: true,
                manager: {
                    firstName: 'Martin',
                    surname: 'Dub',
                },
                feedbacks: [
                    {
                        question: {
                            title: 'Bol dobry cely rok',
                            category: 'Motivation',
                        },
                        answer: 5,
                    },
                    {
                        question: {
                            title: 'Bol poslusny cely rok',
                            category: 'Team Work',
                        },
                        answer: 1,
                    },
                    {
                        question: {
                            title: 'Bol pracovity cely rok',
                            category: 'Reliability',
                        },
                        answer: 3,
                    },
                ],
            },
        ],
    }),
    methods: {
        endRound() {

        },
        sendReminders() {
            
        },
        frCategoryStats(feedbackRound) {
            return feedbackRound.feedbacks.reduce((aggregator, val) => {
                if(aggregator[val.question.category]) {
                    aggregator[val.question.category].max += 5;
                    aggregator[val.question.category].result += val.answer;
                } else {
                    aggregator[val.question.category] = { max: 5, result: val.answer };
                }
                return aggregator;
            }, {});
        },
        frQuestionsStats(feedbackRound, category = '') {
            return feedbackRound.feedbacks.reduce((aggregator, val) => {
                if(category !== '' && val.question.category !== category) {
                    return aggregator;
                }
                if(aggregator[val.question.title]) {
                    aggregator[val.question.title].max += 5;
                    aggregator[val.question.title].result += val.answer;
                } else {
                    aggregator[val.question.title] = { max: 5, result: val.answer, category: val.question.category };
                }
                return aggregator;
            }, {});
        },
        frManagersStats(feedbackRounds) {
            const result = {};
            const self = this;
            feedbackRounds.map(fr => {
                if(!result[self.getName(fr.manager)]) {
                    result[self.getName(fr.manager)] = { categories: {}, questions: {} };
                }
                const cats = self.frCategoryStats(fr);
                Object.keys(cats).map(key => {
                    if(!result[self.getName(fr.manager)].categories[key]) {
                        result[self.getName(fr.manager)].categories[key] = cats[key];
                    } else {
                        result[self.getName(fr.manager)].categories[key].max += cats[key].max;
                        result[self.getName(fr.manager)].categories[key].result = cats[key].result;                        
                    }
                });
                const qts = self.frQuestionsStats(fr);
                Object.keys(qts).map(key => {
                    if(!result[self.getName(fr.manager)].questions[key]) {
                        result[self.getName(fr.manager)].questions[key] = qts[key];
                    } else {
                        result[self.getName(fr.manager)].questions[key].max += qts[key].max;
                        result[self.getName(fr.manager)].questions[key].result = qts[key].result;                        
                    }
                });
            });
            return result;
        },
        getName(obj) {
            return Object.keys(obj).reduce((aggregator, key) => {
                return aggregator + (typeof obj[key] === "string" ? ' ' + obj[key] : '');
            }, '');
        },
        downloadPDF() {
            if(this.pdf) {
                const doc = new jsPDF()
                doc.fromHTML($('#pdfContainer').get(0), 15, 15, {'width': 180});
                doc.output("dataurlnewwindow");
            }
            this.pdf = !this.pdf;
        },
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            
            // ajax.get('/admin/feedbackrounds', {}, ({ data }) => {
                
            // });
        });
    },
    watch: {
        showStatistics(newVal, oldVal) {
            $('.tooltipped').tooltip({delay: 50});
        },
        currentCategory(newVal, oldVal) {
            $('.tooltipped').tooltip({delay: 50});
        },
    },
}
</script>
<style>
.feedback-rounds {
    padding: 20px;
    line-height: 13px;
    cursor: pointer;
    border-bottom: 1px solid #eaeaea;

    b {
        color: #484848;
        font-size: 17px;
    }
    small {
        color: #a7a7a7;
    }
}
.feedback-rounds:hover {
    background: #f1f1f1;
}

</style>
