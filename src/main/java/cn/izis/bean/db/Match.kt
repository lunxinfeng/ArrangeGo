@file:Suppress("PropertyName")

package cn.izis.bean.db

import cn.izis.util.toTime
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class Match(){
        constructor(
                match_name: String,
                sponsor: String,
                organizer: String,
                co_organizer: String,
                supporting: String,
                match_arbitration: String,
                match_address: String,
                match_time_start: Long,
                match_time_end: Long,
                match_referee: String,
                match_arrange: String,
                match_id: Int = 0
        ) : this() {
                this.match_name = match_name
                this.sponsor = sponsor
                this.organizer = organizer
                this.co_organizer = co_organizer
                this.supporting = supporting
                this.match_arbitration = match_arbitration
                this.match_address = match_address
                this.match_time_start = match_time_start
                this.match_time_end = match_time_end
                this.match_referee = match_referee
                this.match_arrange = match_arrange
                this.match_id = match_id
        }

        var match_name:String
                get() = matchNameProperty.get()
                set(value) {matchNameProperty.set(value)}
        val matchNameProperty = SimpleStringProperty()

        var sponsor:String //举办单位
                get() = sponsorProperty.get()
                set(value) {sponsorProperty.set(value)}
        val sponsorProperty = SimpleStringProperty()

        var organizer:String //承办单位
                get() = organizerProperty.get()
                set(value) {organizerProperty.set(value)}
        val organizerProperty = SimpleStringProperty()

        var co_organizer:String //协办单位
                get() = coOrganizerProperty.get()
                set(value) {coOrganizerProperty.set(value)}
        val coOrganizerProperty = SimpleStringProperty()

        var supporting:String //赞助单位
                get() = supportingProperty.get()
                set(value) {supportingProperty.set(value)}
        val supportingProperty = SimpleStringProperty()

        var match_arbitration:String //比赛仲裁
                get() = matchArbitrationProperty.get()
                set(value) {matchArbitrationProperty.set(value)}
        val matchArbitrationProperty = SimpleStringProperty()

        var match_address:String //比赛地点
                get() = matchAddressProperty.get()
                set(value) {matchAddressProperty.set(value)}
        val matchAddressProperty = SimpleStringProperty()

        var match_time_start:Long //比赛时间开始
                get() = matchTimeStartProperty.get()
                set(value) {
                        matchTimeStartProperty.set(value)
                        matchTimeStartStringProperty.set(value.toTime())
                }
        val matchTimeStartProperty = SimpleLongProperty()
        val matchTimeStartStringProperty = SimpleStringProperty()

        var match_time_end:Long //比赛时间结束
                get() = matchTimeEndProperty.get()
                set(value) {
                        matchTimeEndProperty.set(value)
                        matchTimeEndStringProperty.set(value.toTime())
                }
        val matchTimeEndProperty = SimpleLongProperty()
        val matchTimeEndStringProperty = SimpleStringProperty()

        var match_referee:String //裁判长
                get() = matchRefereeProperty.get()
                set(value) {matchRefereeProperty.set(value)}
        val matchRefereeProperty = SimpleStringProperty()

        var match_arrange:String //编排长
                get() = matchArrangeProperty.get()
                set(value) {matchArrangeProperty.set(value)}
        val matchArrangeProperty = SimpleStringProperty()

        var match_id:Int //比赛id
                get() = matchIdProperty.get()
                set(value) {matchIdProperty.set(value)}
        val matchIdProperty = SimpleIntegerProperty()

        var match_round_list:ObservableList<MatchRound> = FXCollections.observableArrayList()

        fun copy(match: Match){
                this.match_name = match.match_name
                this.sponsor = match.sponsor
                this.organizer = match.organizer
                this.co_organizer = match.co_organizer
                this.supporting = match.supporting
                this.match_arbitration = match.match_arbitration
                this.match_address = match.match_address
                this.match_time_start = match.match_time_start
                this.match_time_end = match.match_time_end
                this.match_referee = match.match_referee
                this.match_arrange = match.match_arrange
                this.match_id = match.match_id

                this.match_round_list.clear()
                this.match_round_list.addAll(match.match_round_list)
        }

}