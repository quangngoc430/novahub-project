package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "account_has_day_off_type")
public class AccountHasDayOffType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "year")
    private int year;

    @Column(name = "private_quota")
    private int privateQuota;

    @Column(name = "remaining_time")
    private int remainingTime;

    @Column(name = "day_off_type_id")
    private int dayOffTypeId;

    @Column(name = "account_id")
    private long accountId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DayOffType.class)
    @JoinColumn(name = "day_off_type_id", insertable = false, updatable = false)
    private DayOffType dayOffType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    public void subtractRemainingTime(int numberOfDayOff) {
        this.remainingTime = this.remainingTime - numberOfDayOff;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrivateQuota() {
        return privateQuota;
    }

    public void setPrivateQuota(int privateQuota) {
        this.privateQuota = privateQuota;
    }

    public int getDayOffTypeId() {
        return dayOffTypeId;
    }

    public void setDayOffTypeId(int dayOffTypeId) {
        this.dayOffTypeId = dayOffTypeId;
    }

    public DayOffType getDayOffType() {
        return dayOffType;
    }

    public void setDayOffType(DayOffType dayOffType) {
        this.dayOffType = dayOffType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "AccountHasDayOffType{" +
                "id=" + id +
                ", year=" + year +
                ", remainingTime=" + remainingTime +
                ", accountId=" + accountId +
                '}';
    }
}
