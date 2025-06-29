package pl.ibcgames.smvotifier.response;

public record UserVoteResponse(int id,
                               boolean canClaimReward,
                               String error) {
}
