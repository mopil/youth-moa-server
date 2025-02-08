package com.project.youthmoa.domain.repository

import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entities.entity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UserRepository : JpaRepository<User, Long>, UserJdslRepository {
    fun findByEmail(email: String): User?

    fun findAllByEmail(email: String): List<User>

    fun findAllByNameAndPhone(
        name: String,
        phone: String,
    ): List<User>
}

fun UserRepository.findUniqueByEmailOrThrow(email: String): User {
    val users = findAllByEmail(email)
    if (users.size > 1) {
        throw IllegalStateException("$email 을 가진 사용자가 ${users.size}명 있습니다.")
    }
    if (users.isEmpty()) {
        throw NoSuchElementException()
    }
    return users.firstOrNull() ?: throw NoSuchElementException()
}

fun UserRepository.findByIdOrThrow(id: Long): User = findById(id).orElseThrow()

interface UserJdslRepository {
    fun findAllBySpec(spec: GetAllUsersSpec): Page<User?>
}

@Repository
class UserJdslRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : UserJdslRepository {
    override fun findAllBySpec(spec: GetAllUsersSpec): Page<User?> {
        val user = entity(User::class)
        return kotlinJdslJpqlExecutor.findPage(spec.pageable) {
            select(user)
                .from(user)
                .whereAnd(
                    spec.roles?.let { path(User::role).`in`(it) },
                    spec.gender?.let { path(User::gender).eq(it) },
                    spec.searchKeyword?.let {
                        path(User::name).like(it).or(
                            path(User::email).like(it).or(
                                path(User::phone).like(it),
                            ),
                        )
                    },
                )
                .orderBy(path(User::createdAt).desc())
        }
    }
}
