interface AnimationEntity extends ActivityEntity
{
    Animation createAnimationAction(int repeatCount);
    int getAnimationPeriod();
}
