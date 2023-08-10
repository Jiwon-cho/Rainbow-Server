package com.rainbow.server.domain.expense.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.image.entity.Image
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class DailyExpense(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalId")
    val goal: Goal,
    @Column()
    var comment: String? = null,
    @Column(nullable = false)
    val date: LocalDate,
    var dailyCharacter: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val dailyExpenseId: Long = 0L

    @OneToMany(mappedBy = "dailyExpense", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val expenseMutableList: MutableList<Expense> = mutableListOf()
    val expenseList: List<Expense> get() = expenseMutableList.toList()

    fun addExpense(expense: Expense) {
        expenseMutableList.add(expense)
    }

    fun updateCharacter(dailyCharacter: String) {
        this.dailyCharacter = dailyCharacter
    }

    fun updateComment(comment: String?) {
        this.comment = comment
    }
}

@Entity
class Expense(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customCategoryId")
    val customCategory: CustomCategory,
    // TODO: length 몇으로 설정할 것인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dailyExpenseId")
    val dailyExpense: DailyExpense,
    @Column(nullable = false) var amount: Int,
    @Column(nullable = false) var content: String,

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val expenseId: Long = 0L

    @OneToMany(mappedBy = "expense", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val imageMutableList: MutableList<Image> = mutableListOf()
    val imageList: List<Image> get() = imageMutableList.toList()

    fun modifyExpense(amount: Int, content: String) {
        this.amount = amount
        this.content = content
    }
}

@Entity
class Category(
    name: String,
    imagePath: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name

    var imagePath: String = imagePath

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val customCategoryMutableList: MutableList<CustomCategory> = mutableListOf()
    val customCategoryList: List<CustomCategory> get() = customCategoryMutableList.toList()
}

@Entity
class CustomCategory(
    name: String,
    status: Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    val category: Category?,
    imagePath: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val customCategoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name

    @Column(nullable = false)
    val status: Boolean = status

    var imagePath: String = imagePath

    @OneToMany(mappedBy = "customCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val expenseMutableList: MutableList<Expense> = mutableListOf()
    val expenseList: List<Expense> get() = expenseMutableList.toList()

    fun addExpenseList(expense: Expense) {
        expenseMutableList.add(expense)
    }
}
